package dms.dms.controller;

import dms.dms.domain.MemberEntity;
import dms.dms.dto.MessageDTO;
import dms.dms.repository.MemberRepository;
import dms.dms.service.MessageService;
import dms.dms.Response.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;
    private final MemberRepository memberRepository;

    // @ApiOperation(value = "쪽지 보내기", notes = "쪽지 보내기") <-- swagger v2
    @Operation(summary = "쪽지 보내기", description = "쪽지 보내기")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages")
    public Response<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함

        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() ->{
            return new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        });
        messageDTO.setSenderName(memberEntity.getMemberName());

        return new Response<>("성공", "쪽지를 보냈습니다.", messageService.write(messageDTO));
    }


    @Operation(summary = "받은 편지함 읽기", description = "받은 편지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/received")
    public Response<?> getReceivedMessage() {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() -> {
            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
        });

        return new Response("성공", "받은 쪽지를 불러왔습니다.", messageService.receivedMessage(memberEntity));
    }

    @Operation(summary = "받은 쪽지 삭제하기", description = "받은 쪽지를 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/received/{id}")
    public Response deleteReceivedMessage(@PathVariable("id") Long id) {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() -> {
            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
        });

        return new Response("삭제 성공", "받은 쪽지인, " + id + "번 쪽지를 삭제했습니다.", messageService.deleteMessageByReceiver(id, memberEntity));
    }





    @Operation(summary = "보낸 편지함 읽기", description = "보낸 편지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/sent")
    public Response<?> getSentMessage() {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() -> {
            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
        });

        return new Response("성공", "보낸 쪽지를 불러왔습니다.", messageService.sentMessage(memberEntity));
    }



    @Operation(summary = "보낸 쪽지 삭제하기", description = "보낸 쪽지를 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/sent/{id}")
    public Response<?> deleteSentMessage(@PathVariable("id") Long id) {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() -> {
            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
        });

        return new Response<>("삭제 성공", "보낸 쪽지인, " + id + "번 쪽지를 삭제했습니다.", messageService.deleteMessageBySender(id, memberEntity));
    }

}
