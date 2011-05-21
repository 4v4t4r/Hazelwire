import unittest, sqlite3, os, threading, socket
from Mainframe.ClientHandler import SanityCheckService
from Mainframe.ClientHandler.DatabaseHandler import DatabaseHandler


class SanityCheckTestCase(unittest.TestCase):
    
    def setUp(self):
        try:
            os.remove('temp.db')
        except:
            pass
        self.temp = sqlite3.connect("temp.db")
        self.cursor = self.temp.cursor()
        self.cursor.executescript("""BEGIN TRANSACTION;
CREATE TABLE flagpoints (flag_id NUMERIC, mod_id NUMERIC, points NUMERIC);
CREATE TABLE flags (flag_id NUMERIC, mod_id NUMERIC, team_id NUMERIC, flag TEXT);
CREATE TABLE modules (serviceport NUMERIC, id INTEGER PRIMARY KEY, name TEXT, numFlags NUMERIC, deployscript TEXT);
INSERT INTO modules VALUES(31337, 1,'test1',5,'deploy/deployFlags');
INSERT INTO modules VALUES(61281, 2,'test2',2,'deploy/install.py');
CREATE TABLE teams (id INTEGER PRIMARY KEY, name TEXT, VMip NUMERIC);
INSERT INTO teams VALUES(1,'lokale lutsers','127.0.0.1');
CREATE TABLE config (config_name TEXT, type TEXT, value TEXT);
INSERT INTO config VALUES('normal_interval','sanitycheck',3);
INSERT INTO config VALUES('p2p_interval','sanitycheck',10);
CREATE TABLE evil_teams (IP TEXT, port NUMERIC, time NUMERIC);
COMMIT;""")
        self.temp.commit()
        self.cursor.close()
        self.temp.close()
        self.db = DatabaseHandler("temp.db")
        self.sanitychecker = SanityCheckService.SanityChecker(3,10,'temp.db')
        
    def tearDown(self):
        os.remove("temp.db")
        
    def listenPort(self, port):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind(('127.0.0.1',port))
        s.listen(1)
        conn, addr = s.accept()
        data = conn.recv(1024).strip()
        
    def test_sanitycheck_1_NormalCheck_notlistening(self):
        self.sanitychecker.NormalCheck() #this should have added 127.0.0.1 to the database.
        db = sqlite3.connect("temp.db")
        c = db.cursor()
        res = c.execute("SELECT IP, port FROM evil_teams").fetchall()
        expected_res = [('127.0.0.1',31337),('127.0.0.1',61281)]
        self.assertEqual(res, expected_res)
        
    def test_sanitycheck_2_NormalCheck_listening(self):
        #start listening
        t1 = threading.Thread(target=self.listenPort, args=[31337])
        t1.start()
        t2 = threading.Thread(target=self.listenPort, args=[61281])
        t2.start()
        self.sanitychecker.NormalCheck()
        db = sqlite3.connect("temp.db")
        c = db.cursor()
        res = c.execute("SELECT IP, port FROM evil_teams").fetchall()
        self.assertEqual(res,[]) #this should be empty as the services are running
        