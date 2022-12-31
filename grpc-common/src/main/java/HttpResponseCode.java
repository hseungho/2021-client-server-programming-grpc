public enum HttpResponseCode {

    //200
    OK(200), // 요청이 성공적으로 처리되었음.
    CREATED(201), // 요청이 성공적이었으며, 새로운 리소스가 생성되었음.
    NO_CONTENT(204), // 요청에 대해 리턴할 콘텐츠는 없지만, 헤더는 의미있을 수 있음.

    // 400
    BAD_REQUEST(400), // 잘못된 문법으로 요청하여 서버가 이해할 수 없음.
    FORBIDDEN(403), // 클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않음.
    NOT_FOUND(404), // 서버는 요청받은 리소스를 찾을 수 없음.

    // 500
    INTERNAL_SERVER_ERROR(500); // 서버 내의 문제가 발생하였음

    int code;
    HttpResponseCode(int code) {
        this.code = code;
    }
    int getCode() {
        return code;
    }
}
