# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
import uuid
import DBSession
from models import model
from common.TransformToList import trans_params

class SBooks():
    def __init__(self):
        try:
            self.session = DBSession.db_session()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"

    def get_all_book(self):
        all_book = None
        try:
            all_book = self.session.query(model.Books.Bid, model.Books.Bno, model.Books.Bname, model.Books.Bstatus,
                                          model.Books.Btype, model.Books.Babo).all()
        except Exception as e:
            self.session.rollback()
            print "=====================message========================"
            print e.message
            print "=====================message========================"
        finally:
            self.session.close()
        return all_book

    def get_book_abo_by_bid(self, bid):
        book_abo = None
        try:
            book_abo = self.session.query(model.Books.Bno, model.Books.Bname, model.Books.Bstatus,
                                          model.Books.Btype, model.Books.Babo).filter_by(Bid=bid).first()
        except Exception as e:
            self.session.rollback()
            print "=====================message========================"
            print e.message
            print "=====================message========================"
        finally:
            self.session.close()
        return book_abo

    def get_all_book_by_uid(self, uid):
        all_book = None
        try:
            all_book = self.session.query(model.UBooks.Bid).filter_by(Uid=uid).all()
        except Exception as e:
            self.session.rollback()
            print "=====================message========================"
            print e.message
            print "=====================message========================"
        finally:
            self.session.close()
        return all_book

    def add_user_book(self, uid, bid):
        """
        :param uid:
        :param bid:
        :return:
        """
        try:
            new_book_user = model.UBooks()
            new_book_user.UBid = str(uuid.uuid4())
            new_book_user.Uid = uid
            new_book_user.Bid = bid
            new_book_user.UBtime = "123456"
            self.session.add(new_book_user)
            self.session.commit()
            self.session.close()
            return True
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"
            self.session.rollback()
            self.session.close()
            return False

    def update_bstatus(self, bid, books):
        try:
            self.session.query(model.Books).filter_by(Bid=bid).update(books)
            self.session.commit()
            self.session.close()
            return True
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"
            self.session.rollback()
            self.session.close()
            return False