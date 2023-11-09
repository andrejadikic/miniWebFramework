package domain.model.webframework.response;

import domain.model.webframework.request.Header;
import lombok.Getter;

@Getter
public abstract class Response {
    protected Header header;

    public Response() {
        this.header = new Header();
    }

    public abstract String render();
}
