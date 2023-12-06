package dms.service;


import dms.domain.File;
import dms.dto.FileDto;
import dms.repository.BoardRepository;
import dms.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long uploadFile(FileDto fileDto) {
        File file = new File();
        file.setOriginFilename(fileDto.getOriginFilename());
        file.setFilename(fileDto.getFilename());
        file.setFilePath(fileDto.getFilePath());
        return fileRepository.save(file).getFileId();
    } // upload한 파일에 대한 정보 기록

    @Transactional
    public Long fetchBoard(Long fileId, Long boardId){
        // null 검사하기
        File file = fileRepository.findByFileId(fileId).get();
        file.setBoard(boardRepository.findById(boardId).get());
        return file.getFileId();
    }

    @Transactional
    public FileDto getFile(Long fileId) {
        File file = fileRepository.findById(fileId).get();
        FileDto fileDto = FileDto.toDto(file);
        return fileDto;
    } // id값을 사용하여 파일에 대한 정보를 가져옴
}
