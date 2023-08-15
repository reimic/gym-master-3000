package gymmaster3000.member.adapter.out.db;

import gymmaster3000.member.application.port.FindAllMembersPort;
import gymmaster3000.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class FindAllMembersAdapter implements FindAllMembersPort {

    private final MemberRepository memberRepository;

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
