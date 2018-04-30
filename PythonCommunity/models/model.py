# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, create_engine, Integer, String, Text, Float
from config import dbconfig as cfg
import pymysql

DB_PARAMS = "{0}://{1}:{2}@{3}/{4}?charset={5}".format(
    cfg.sqlenginename, cfg.username, cfg.password, cfg.host, cfg.database, cfg.charset)
mysql_engine = create_engine(DB_PARAMS, echo=True)
Base = declarative_base()

class Users(Base):
    __tablename__ = "Users"
    Uid = Column(String(64), primary_key=True)
    Utel = Column(String(14), nullable=False)
    Upwd = Column(String(64), nullable=False)
    Utype = Column(Integer, nullable=False) # 101普通用户 102管理员
    Usex = Column(Integer) # 201男 202女
    Ucardno = Column(String(18))
    Ucoin = Column(Float)
    Ulive = Column(Text)
    Uname = Column(String(32))

class Books(Base):
    __tablename__ = "Books"
    Bid = Column(String(64), primary_key=True)
    Bno = Column(String(32), nullable=False)
    Bname = Column(String(128), nullable=False)
    Bstatus = Column(Integer, nullable=False) # 301可借阅 302不可借阅
    Btype = Column(Integer, nullable=False) # 401历史类 402军事类 403经管类 404文学类 405其他
    Babo = Column(Text)

class UBooks(Base):
    __tablename__ = "UBooks"
    UBid = Column(String(64), primary_key=True)
    Uid = Column(String(64), nullable=False)
    Bid = Column(String(64), nullable=False)
    UBtime = Column(String(14), nullable=False)

class Locations(Base):
    __tablename__ = "Locations"
    Lid = Column(String(64), primary_key=True)
    Lno = Column(String(32), nullable=False)
    Lstatus = Column(Integer, nullable=False) # 501可用 502不可用
    Labo = Column(Text)

class ULocations(Base):
    __tablename__ = "ULocations"
    ULid = Column(String(64), primary_key=True)
    Uid = Column(String(64), nullable=False)
    Lid = Column(String(64), nullable=False)
    ULtimemin = Column(String(14), nullable=False)
    ULtimemax = Column(String(14))

class Waters(Base):
    __tablename__ = "Waters"
    Wid = Column(String(64), primary_key=True)
    Uid = Column(String(64), nullable=False)
    Wyear = Column(Integer, nullable=False)
    Wmonth = Column(Integer, nullable=False)
    Wpay = Column(Float, nullable=False)
    Wstatus = Column(Integer, nullable=False) # 601未缴费 602已缴费

class Cards(Base):
    __tablename__ = "Cards"
    Cid = Column(String(64), primary_key=True)
    Uid = Column(String(64), nullable=False)
    Cname = Column(String(128), nullable=False)
    Cabo = Column(Text, nullable=False)
    Cstatus = Column(Integer, nullable=False) # 701已审核 702未审核
    Ctime = Column(String(14), nullable=False)

class Reviews(Base):
    __tablename__ = "Reviews"
    Rid = Column(String(64), primary_key=True)
    Uid = Column(String(64), nullable=False)
    Cid = Column(String(64), nullable=False)
    RUid = Column(String(64), nullable=False)
    Rabo = Column(Text, nullable=False)
    Rtime = Column(String(14), nullable=False)

class databse_deal():
    def __init__(self):
        self.conn = pymysql.connect(host=cfg.host, user=cfg.username, passwd=cfg.password, charset=cfg.charset)
        self.cursor = self.conn.cursor()

    def create_database(self):
        sql = "create database if not exists {0} DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;".format(
            cfg.database)
        print sql
        try:
            self.cursor.execute(sql)
        except Exception, e:
            print(e)
        finally:
            self.conn_close()

    def drop_database(self):
        sql = "drop database if exists {0} ;".format(
            cfg.database)
        print sql
        try:
            self.cursor.execute(sql)
        except Exception, e:
            print(e)

        finally:
            self.conn_close()

    def conn_close(self):
        self.conn.close()


def create():
    databse_deal().create_database()
    Base.metadata.create_all(mysql_engine)


def drop():
    databse_deal().drop_database()


if __name__ == "__main__":
    '''
    运行该文件就可以在对应的数据库里生成本文件声明的所有table
    如果需要清除数据库，输入drop
    如果需要创建数据库 输入任意不包含drop的字符
    '''
    action = raw_input("create database?")
    if "drop" in action:
        drop()
    else:
        create()