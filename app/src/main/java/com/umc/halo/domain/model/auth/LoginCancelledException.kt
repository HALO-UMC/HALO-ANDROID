package com.umc.halo.domain.model.auth

/**
 * 사용자가 소셜 로그인 창을 직접 닫았을 때 발생하는 예외처리
 *
 * 카카오/구글 SDK 는 취소를 각자 다른 예외로 던지는데 화면에서 그걸 일일이 구분하지 않도록
 * DataSource 에서 이 예외로 통일해 raise
 */
class LoginCancelledException(cause: Throwable? = null) :
    Exception("사용자가 로그인을 취소했습니다.", cause)
