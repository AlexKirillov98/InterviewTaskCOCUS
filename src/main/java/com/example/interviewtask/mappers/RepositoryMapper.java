package com.example.interviewtask.mappers;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.Branch;
import org.openapitools.model.Repository;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    @Mapping(target = "ownerLogin", expression = "java(repository.getOwnerName())")
    @Mapping(target = "branches", source = "branchesList")
    Repository map(GHRepository repository, Collection<GHBranch> branchesList);

    @Mapping(target = "latestCommitSHA", expression = "java(branch.getSHA1())")
    Branch map(GHBranch branch);
}
