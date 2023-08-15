package gymmaster3000.member.adapter.out.db;

import gymmaster3000.member.application.port.FindMemberPort;
import gymmaster3000.member.domain.entity.Member;
import gymmaster3000.member.domain.valueobject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class FindMemberAdapter implements FindMemberPort {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findBy(final MemberId memberId) {
        return memberRepository.findById(memberId);
    }

}
