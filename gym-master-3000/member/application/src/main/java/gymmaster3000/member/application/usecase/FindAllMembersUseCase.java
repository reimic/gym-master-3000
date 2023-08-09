package gymmaster3000.member.application.usecase;

import gymmaster3000.member.application.port.FindAllMembersPort;
import gymmaster3000.member.domain.entity.MemberView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class FindAllMembersUseCase {

    private final FindAllMembersPort port;

    public List<MemberView> apply(GetAllMembersQuery query) {
        return port.findAll()
                   .stream()
                   .map(MemberView::from)
                   .toList();
    }

    public record GetAllMembersQuery() {

    }

}
