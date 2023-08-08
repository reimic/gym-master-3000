package gymmaster3000.member.application.port;

import gymmaster3000.member.domain.entity.Member;

import java.util.List;

public interface FindAllMembersPort {

    List<Member> findAll();

}
