# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
import uuid
import DBSession
from models import model
from common.TransformToList import trans_params

class SCards():
    def __init__(self):
        try:
            self.session = DBSession.db_session()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"

    def get_all_card(self):
        all_card = None
        try:
            all_card = self.session.query(model.Cards.Cid, model.Cards.Uid, model.Cards.Cname, model.Cards.Cabo,
                                          model.Cards.Cstatus, model.Cards.Ctime).all()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"
            self.session.rollback()
        finally:
            self.session.close()
        return all_card

    def get_card_abo_by_cid(self, cid):
        card_abo = None
        try:
            card_abo = self.session.query(model.Cards.Uid, model.Cards.Cname, model.Cards.Cabo,
                                          model.Cards.Cstatus, model.Cards.Ctime).filter_by(Cid=cid).first()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"
            self.session.rollback()
        finally:
            self.session.close()
        return card_abo

    def add_new_card(self, uid, cname, cabo):
        """
        :param uid:
        :param cname:
        :param cabo:
        :return:
        """
        try:
            new_card = model.Cards()
            new_card.Cid = str(uuid.uuid4())
            new_card.Uid = uid
            new_card.Cname = cname
            new_card.Cabo = cabo
            new_card.Cstatus = 701
            import datetime
            new_card.Ctime = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
            self.session.add(new_card)
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