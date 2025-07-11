package gr.aueb.cf.e_commerce_app.rest;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.core.exceptions.ValidationException;
import gr.aueb.cf.e_commerce_app.dto.RegionInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RegionReadOnlyDto;
import gr.aueb.cf.e_commerce_app.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegionRestController.class);
    private final RegionService regionService;

    @Operation(
            summary = "Creates a new region",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "New region created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegionReadOnlyDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Region already exists", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<RegionReadOnlyDto> saveRegion(@Valid @RequestBody RegionInsertDto insertDto, BindingResult bindingResult)
            throws ValidationException, AppObjectAlreadyExistsException {

        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        RegionReadOnlyDto regionReadOnlyDto = regionService.saveRegion(insertDto);
        LOGGER.info("New region inserted");
        return new ResponseEntity<>(regionReadOnlyDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Removes a region",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Region removed",
                            content = @Content
                    ),
                    @ApiResponse(responseCode = "404", description = "Region not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Cannot remove the region", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void removeRegion(@PathVariable Long id) throws AppObjectNotFoundException, AppObjectIllegalStateException {
        regionService.removeRegion(id);
        LOGGER.info("Region has removed");
    }

    @Operation(
            summary = "Get all regions sorted by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Regions found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegionReadOnlyDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<RegionReadOnlyDto>> getAllRegions() {
        List<RegionReadOnlyDto> regionsList = regionService.getAllRegions();
        return new ResponseEntity<>(regionsList, HttpStatus.OK);
    }
}
