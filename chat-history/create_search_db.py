#!/usr/bin/env python3
"""
Create SQLite database for ultra-fast chat history search
Optimized for AI assistants to query chat history efficiently
"""

import sqlite3
import json
import os
from datetime import datetime

DB_PATH = '/home/ubuntu/match-mania/chat-history/search.db'

def create_database():
    """Create optimized search database"""
    
    # Remove old database if exists
    if os.path.exists(DB_PATH):
        os.remove(DB_PATH)
    
    # Create connection
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()
    
    # Create tables
    cursor.execute('''
        CREATE TABLE sessions (
            id INTEGER PRIMARY KEY,
            session_id TEXT UNIQUE NOT NULL,
            filename TEXT NOT NULL,
            date TEXT NOT NULL,
            time_period TEXT,
            size_kb INTEGER,
            lines INTEGER,
            status TEXT,
            priority TEXT,
            quick_summary TEXT
        )
    ''')
    
    cursor.execute('''
        CREATE TABLE topics (
            id INTEGER PRIMARY KEY,
            session_id TEXT NOT NULL,
            topic TEXT NOT NULL,
            FOREIGN KEY (session_id) REFERENCES sessions(session_id)
        )
    ''')
    
    cursor.execute('''
        CREATE TABLE keywords (
            id INTEGER PRIMARY KEY,
            session_id TEXT NOT NULL,
            keyword TEXT NOT NULL,
            FOREIGN KEY (session_id) REFERENCES sessions(session_id)
        )
    ''')
    
    cursor.execute('''
        CREATE TABLE versions (
            id INTEGER PRIMARY KEY,
            session_id TEXT NOT NULL,
            version TEXT NOT NULL,
            FOREIGN KEY (session_id) REFERENCES sessions(session_id)
        )
    ''')
    
    cursor.execute('''
        CREATE TABLE key_changes (
            id INTEGER PRIMARY KEY,
            session_id TEXT NOT NULL,
            change_description TEXT NOT NULL,
            FOREIGN KEY (session_id) REFERENCES sessions(session_id)
        )
    ''')
    
    # Insert session data
    sessions = [
        ('session-001', 'session-2025-10-05-v2.3.13-to-v2.3.15.md', '2025-10-05', 
         'morning-afternoon', 23, 812, 'active', 'high',
         'Three version releases fixing card display on tablets and phones, adding rotation support, and creating initial automated testing system v1.0'),
        
        ('session-002', 'session-2025-10-05-emulator-testing-v2.0.0.md', '2025-10-05',
         'evening-extended', 22, 502, 'active', 'critical',
         'Major release v2.0.0 expanding package manager support from 5 to 10, achieving 95% Linux coverage, adding CI/CD automation, and integrating website showcase'),
        
        ('session-003', 'session-2025-10-05-website-updates.md', '2025-10-05',
         'evening-part1', 16, 540, 'active', 'medium',
         'Redesigned website testing page to match main site design, improved layout and responsiveness, added professional styling'),
        
        ('session-004', 'session-2025-10-05-final-cleanup.md', '2025-10-05',
         'evening-part2', 17, 602, 'active', 'medium',
         'Complete repository cleanup organizing 82 documentation files into docs/ structure with comprehensive navigation'),
        
        ('session-005', 'session-2025-10-05-complete.md', '2025-10-05',
         'all-day', 16, 527, 'active', 'highest',
         'Master summary document covering entire day\'s work across all sessions with complete statistics and future roadmap')
    ]
    
    cursor.executemany('INSERT INTO sessions VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)', sessions)
    
    # Insert topics
    topics = [
        ('session-001', 'app_versions'),
        ('session-001', 'card_layout_fixes'),
        ('session-001', 'tablet_optimization'),
        ('session-001', 'testing_system_v1.0'),
        ('session-002', 'package_managers'),
        ('session-002', 'linux_support'),
        ('session-002', 'ci_cd_automation'),
        ('session-002', 'version_v2.0.0'),
        ('session-002', 'website_integration'),
        ('session-003', 'website_design'),
        ('session-003', 'testing_page_redesign'),
        ('session-004', 'repository_cleanup'),
        ('session-004', 'documentation_organization'),
        ('session-005', 'master_summary'),
        ('session-005', 'complete_overview')
    ]
    
    cursor.executemany('INSERT INTO topics VALUES (NULL, ?, ?)', topics)
    
    # Insert keywords
    keywords = [
        ('session-001', 'card_display'),
        ('session-001', 'tablet_width'),
        ('session-001', 'rotation'),
        ('session-001', 'v2.3.13'),
        ('session-001', 'v2.3.14'),
        ('session-001', 'v2.3.15'),
        ('session-002', 'apt'),
        ('session-002', 'dnf'),
        ('session-002', 'yum'),
        ('session-002', 'pacman'),
        ('session-002', 'zypper'),
        ('session-002', 'apk'),
        ('session-002', 'emerge'),
        ('session-002', 'xbps'),
        ('session-002', 'nix'),
        ('session-002', 'brew'),
        ('session-002', 'v2.0.0'),
        ('session-002', '95_percent_coverage'),
        ('session-002', 'github_actions'),
        ('session-003', 'testing_html'),
        ('session-003', 'responsive_design'),
        ('session-004', 'docs_structure'),
        ('session-004', '82_files'),
        ('session-005', 'future_roadmap'),
        ('session-005', 'statistics')
    ]
    
    cursor.executemany('INSERT INTO keywords VALUES (NULL, ?, ?)', keywords)
    
    # Create indexes for fast searching
    cursor.execute('CREATE INDEX idx_topics ON topics(topic)')
    cursor.execute('CREATE INDEX idx_keywords ON keywords(keyword)')
    cursor.execute('CREATE INDEX idx_versions ON versions(version)')
    cursor.execute('CREATE INDEX idx_session_priority ON sessions(priority)')
    cursor.execute('CREATE INDEX idx_session_date ON sessions(date)')
    
    # Create full-text search virtual table
    cursor.execute('''
        CREATE VIRTUAL TABLE search_fts USING fts5(
            session_id,
            filename,
            content,
            tokenize = 'porter ascii'
        )
    ''')
    
    # Commit and close
    conn.commit()
    conn.close()
    
    print("✅ Created search.db with optimized indexes")
    print(f"   Location: {DB_PATH}")
    print("   Tables: sessions, topics, keywords, versions, key_changes, search_fts")
    print("   Indexes: Optimized for fast AI querying")

if __name__ == '__main__':
    create_database()
