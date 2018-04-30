# *- coding:utf8 *-

apis_wrong = {
    "status": 405,
    "status_code": 405000,
    "messages": "找不到对应的api"
}

system_error = {
    "status": 404,
    "messages": "系统异常"
}

no_tel = {
    "status": 405,
    "status_code": 405100,
    "messages": "该手机号未注册"
}

wrong_pwd = {
    "status": 405,
    "status_code": 405101,
    "messages": "密码错误"
}

repeat_tel = {
    "status": 405,
    "status_code": 405102,
    "messages": "该手机号已注册"
}

register_ok = {
    "status": 200,
    "messages": "注册成功"
}

login_ok = {
    "status": 200,
    "messages": "登录成功"
}

response_ok = {
    "status": 200,
    "messages": "获取数据成功"
}

success_update_uinfo = {
    "status": 200,
    "messages": "更新个人信息成功"
}

param_miss = {
    "status": 405,
    "status_code": 405001,
    "messages": "参数缺失"
}

no_permission = {
    "status": 405,
    "status_code": 405201,
    "messages": "没有权限"
}

update_task_success = {
    "status": 200,
    "messages": "更新状态成功"
}