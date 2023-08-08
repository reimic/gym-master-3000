package gymmaster3000.member.application.port;

import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberId;

import java.util.Optional;

public interface FindMemberPort {

    Optional<Member> findBy(MemberId memberId);

}
