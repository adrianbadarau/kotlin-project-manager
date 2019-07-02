package com.for_the_love_of_code.kotlin_pm.web.rest.errors

class EmailAlreadyUsedException :
    BadRequestAlertException(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists") {

    companion object {
        private const val serialVersionUID = 1L
    }
}
