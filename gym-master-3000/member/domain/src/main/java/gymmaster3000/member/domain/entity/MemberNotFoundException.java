package gymmaster3000.member.domain.entity;

import java.util.UUID;

public class MemberNotFoundException extends RuntimeException {

    public static final String MEMBER_NOT_FOUND = "No member with id=%s was found.";

    public MemberNotFoundException(UUID memberId) {
        super(MEMBER_NOT_FOUND.formatted(memberId));
    }

}
