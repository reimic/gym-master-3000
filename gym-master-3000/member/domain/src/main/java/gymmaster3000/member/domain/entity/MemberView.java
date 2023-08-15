package gymmaster3000.member.domain.entity;

import java.util.UUID;

public record MemberView(UUID memberId, String memberEmail) {

    public static MemberView from(Member member) {
        return new MemberView(
                member.getMemberId()
                      .value(),
                member.getMemberEmail()
                      .value());
    }

    public String toJSON() {
        return "{\"memberId\":\"" + memberId + "\",\"memberEmail\":\"" + memberEmail + "\"}";
    }

}
