package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.RegionInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RegionReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.UserMoreInfo;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import gr.aueb.cf.e_commerce_app.repository.RegionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private RegionService regionService;

    @Test
    void saveRegion_shouldSaveAndReturnDto_whenRegionDoesNotExist() throws Exception {
        RegionInsertDto insertDto = new RegionInsertDto("Europe");
        Region region = new Region();
        RegionReadOnlyDto dto = new RegionReadOnlyDto(1L, "Europe");

        Mockito.when(regionRepository.findByName("Europe")).thenReturn(Optional.empty());
        Mockito.when(mapper.mapToRegionEntity(insertDto)).thenReturn(region);
        Mockito.when(regionRepository.save(region)).thenReturn(region);
        Mockito.when(mapper.mapToRegionReadOnlyDto(region)).thenReturn(dto);

        RegionReadOnlyDto result = regionService.saveRegion(insertDto);

        assertEquals(dto, result);
        verify(regionRepository).save(region);
    }

    @Test
    void saveRegion_shouldThrowException_whenRegionAlreadyExists() {
        RegionInsertDto insertDto = new RegionInsertDto("Asia");
        Region existingRegion = new Region();

        Mockito.when(regionRepository.findByName("Asia")).thenReturn(Optional.of(existingRegion));

        assertThrows(AppObjectAlreadyExistsException.class, () -> regionService.saveRegion(insertDto));
        verify(regionRepository, never()).save(any());
    }

    @Test
    void removeRegion_shouldDelete_whenRegionExistsAndNotUsed() throws Exception {
        Region region = new Region();
        region.setUserMoreInfos(Collections.emptySet());

        Mockito.when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        regionService.removeRegion(1L);

        verify(regionRepository).delete(region);
    }

    @Test
    void removeRegion_shouldThrowIllegalState_whenRegionIsUsed() {
        Region region = new Region();
        region.setUserMoreInfos(Set.of(new UserMoreInfo())); // Dummy relation

        Mockito.when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        assertThrows(AppObjectIllegalStateException.class, () -> regionService.removeRegion(1L));
        verify(regionRepository, never()).delete((Region) any());
    }

    @Test
    void removeRegion_shouldThrowNotFound_whenRegionDoesNotExist() {
        Mockito.when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppObjectNotFoundException.class, () -> regionService.removeRegion(1L));
        verify(regionRepository, never()).delete((Region) any());
    }

    @Test
    void getAllRegions_shouldReturnAllMappedDtos() {
        Region region1 = new Region(1L, "Europe");
        Region region2 = new Region(2L, "Asia");

        RegionReadOnlyDto dto1 = new RegionReadOnlyDto(1L, "Europe");
        RegionReadOnlyDto dto2 = new RegionReadOnlyDto(2L, "Asia");

        Mockito.when(regionRepository.findAll(Sort.by("id"))).thenReturn(List.of(region1, region2));
        Mockito.when(mapper.mapToRegionReadOnlyDto(region1)).thenReturn(dto1);
        Mockito.when(mapper.mapToRegionReadOnlyDto(region2)).thenReturn(dto2);

        List<RegionReadOnlyDto> result = regionService.getAllRegions();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }
}
