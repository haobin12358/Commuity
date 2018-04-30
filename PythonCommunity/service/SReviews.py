# *- coding:utf8 *-
import sys
import os
sys.path.append(os.path.dirname(os.getcwd()))
import uuid
import DBSession
from models import model
from common.TransformToList import trans_params

class SReviews():
    def __init__(self):
        try:
            self.session = DBSession.db_session()
        except Exception as e:
            print "=====================message========================"
            print e.message
            print "=====================message========================"

    def get_review_by_cid(self, cid):
        all_review = None
        try:
            all_review = self.session.query(model.Reviews.Uid, model.Reviews.RUid, model.Reviews.Rabo,
                                            model.Reviews.Rtime).filter_by(Cid=cid).all()
        except Exception as e:
            self.session.rollback()
            print "=====================message========================"
            print e.message
            print "=====================message========================"
        finally:
            self.session.close()
        return all_review

    def new_review(self, uid, cid, ruid, rabo):
        """
        :param uid:
        :param cid:
        :param ruid:
        :param rabo:
        :return:
        """
        try:
            new_a_review = model.Reviews()
            new_a_review.Rid = str(uuid.uuid4())
            new_a_review.Uid = uid
            new_a_review.Cid = cid
            new_a_review.RUid = ruid
            new_a_review.Rabo = rabo
            import datetime
            new_a_review.Rtime = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
            self.session.add(new_a_review)
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
