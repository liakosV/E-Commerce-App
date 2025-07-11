package gr.aueb.cf.e_commerce_app.service;

import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectIllegalStateException;
import gr.aueb.cf.e_commerce_app.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.e_commerce_app.dto.RegionInsertDto;
import gr.aueb.cf.e_commerce_app.dto.RegionReadOnlyDto;
import gr.aueb.cf.e_commerce_app.mapper.Mapper;
import gr.aueb.cf.e_commerce_app.model.static_data.Region;
import gr.aueb.cf.e_commerce_app.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public RegionReadOnlyDto saveRegion(RegionInsertDto insertDto) throws AppObjectAlreadyExistsException {

        if (regionRepository.findByName(insertDto.getName()).isPresent()) {
            throw new AppObjectAlreadyExistsException("Region", "The region with name: " + insertDto.getName() + " already exists");
        }

        Region region = regionRepository.save(mapper.mapToRegionEntity(insertDto));
        return mapper.mapToRegionReadOnlyDto(region);
    }

    @Transactional(rollbackOn = Exception.class)
    public void removeRegion(Long id) throws AppObjectNotFoundException, AppObjectIllegalStateException {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Region", "The region is not found"));

        if (!region.getAllUserMoreInfos().isEmpty()) throw new AppObjectIllegalStateException("Region", "You cannot remove a region that is used by users");

        regionRepository.delete(region);
    }

    public List<RegionReadOnlyDto> getAllRegions() {
        return regionRepository.findAll(Sort.by("id")).stream()
                .map(mapper::mapToRegionReadOnlyDto)
                .toList();
    }
}
