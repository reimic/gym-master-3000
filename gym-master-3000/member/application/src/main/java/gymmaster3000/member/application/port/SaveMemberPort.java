package gymmaster3000.member.application.port;

import gymmaster3000.member.domain.entity.Member;

import java.util.UUID;

public interface SaveMemberPort {

    UUID save(Member member);

}
