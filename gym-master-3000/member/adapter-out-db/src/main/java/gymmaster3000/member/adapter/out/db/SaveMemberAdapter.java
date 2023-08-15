package gymmaster3000.member.adapter.out.db;

import gymmaster3000.member.application.port.SaveMemberPort;
import gymmaster3000.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
@RequiredArgsConstructor
public class SaveMemberAdapter implements SaveMemberPort {

    private final MemberRepository memberRepository;

    @Override
    public UUID save(final Member member) {
        return memberRepository.save(member)
                               .getMemberId()
                               .value();
    }

}
