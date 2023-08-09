package gymmaster3000.member.domain.entity;

import gymmaster3000.member.domain.converter.MemberEmailConverter;
import gymmaster3000.member.domain.converter.MemberIdConverter;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import gymmaster3000.member.domain.valueobject.MemberId;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @Convert(converter = MemberIdConverter.class)
    private MemberId memberId;
    @Column(nullable = false, unique = true)
    @Convert(converter = MemberEmailConverter.class)
    private MemberEmail memberEmail;

    public static Member create() {
        Member member = new Member();
        member.memberId = new MemberId(UUID.randomUUID());
        return member;
    }

    public Member withNewEmail(@NonNull MemberEmail memberEmail) {
        if (!memberEmail.equals(this.memberEmail)) {
            return new Member(memberId, memberEmail);
        } else {
            return this;
        }
    }

}
