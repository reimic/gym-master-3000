package gymmaster3000.member.domain.entity;

import gymmaster3000.member.domain.converter.MemberEmailConverter;
import gymmaster3000.member.domain.valueobject.MemberEmail;
import gymmaster3000.member.domain.valueobject.MemberId;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Entity
@Getter
public class Member {

    @Id
    private MemberId memberId;
    @Column(nullable = false, unique = true)
    @Convert(converter = MemberEmailConverter.class)
    private MemberEmail memberEmail;

    private Member(MemberId memberId, MemberEmail memberEmail) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
    }

    private Member() {
    }

    public static Member signUp() {
        Member member = new Member();
        member.memberId = MemberId.of(UUID.randomUUID());
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
