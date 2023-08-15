package gymmaster3000.member.adapter.out.db;

import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, MemberId> {

}
