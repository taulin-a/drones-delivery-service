package org.drones.delivery.model.input;

import com.beust.jcommander.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilePathInputDTO {
    @Parameter(names = {"--file", "-f"}, required = true)
    private String pathStr;
}
