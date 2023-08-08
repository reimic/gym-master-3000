package gymmaster3000.member.domain.entity;

import gymmaster3000.member.domain.valueobject.MemberEmail;
import gymmaster3000.member.domain.valueobject.MemberId;

public record MemberView(MemberId memberId, MemberEmail memberEmail) {

    public static MemberView from(Member member) {
        return new MemberView(
                member.getMemberId(),
                member.getMemberEmail());
    }

}
