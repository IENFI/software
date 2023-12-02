package dms.dms.dto;

import dms.dms.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private Long fileId;
    private String originFilename;
    private String filename;
    private String filePath;

    public static FileDto toDto(File file) {
        return new FileDto(
                file.getFileId(),
                file.getOriginFilename(),
                file.getFilename(),
                file.getFilePath()
        );
    }
}
