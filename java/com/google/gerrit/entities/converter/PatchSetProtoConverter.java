begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.entities.converter
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|Immutable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|proto
operator|.
name|Entities
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_enum
annotation|@
name|Immutable
DECL|enum|PatchSetProtoConverter
specifier|public
enum|enum
name|PatchSetProtoConverter
implements|implements
name|ProtoConverter
argument_list|<
name|Entities
operator|.
name|PatchSet
argument_list|,
name|PatchSet
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
DECL|field|patchSetIdConverter
specifier|private
specifier|final
name|ProtoConverter
argument_list|<
name|Entities
operator|.
name|PatchSet_Id
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|patchSetIdConverter
init|=
name|PatchSetIdProtoConverter
operator|.
name|INSTANCE
decl_stmt|;
DECL|field|objectIdConverter
specifier|private
specifier|final
name|ProtoConverter
argument_list|<
name|Entities
operator|.
name|ObjectId
argument_list|,
name|ObjectId
argument_list|>
name|objectIdConverter
init|=
name|ObjectIdProtoConverter
operator|.
name|INSTANCE
decl_stmt|;
DECL|field|accountIdConverter
specifier|private
specifier|final
name|ProtoConverter
argument_list|<
name|Entities
operator|.
name|Account_Id
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accountIdConverter
init|=
name|AccountIdProtoConverter
operator|.
name|INSTANCE
decl_stmt|;
annotation|@
name|Override
DECL|method|toProto (PatchSet patchSet)
specifier|public
name|Entities
operator|.
name|PatchSet
name|toProto
parameter_list|(
name|PatchSet
name|patchSet
parameter_list|)
block|{
name|Entities
operator|.
name|PatchSet
operator|.
name|Builder
name|builder
init|=
name|Entities
operator|.
name|PatchSet
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
name|patchSetIdConverter
operator|.
name|toProto
argument_list|(
name|patchSet
operator|.
name|id
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setCommitId
argument_list|(
name|objectIdConverter
operator|.
name|toProto
argument_list|(
name|patchSet
operator|.
name|commitId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setUploaderAccountId
argument_list|(
name|accountIdConverter
operator|.
name|toProto
argument_list|(
name|patchSet
operator|.
name|uploader
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setCreatedOn
argument_list|(
name|patchSet
operator|.
name|createdOn
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|groups
init|=
name|patchSet
operator|.
name|groups
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|groups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|builder
operator|.
name|setGroups
argument_list|(
name|PatchSet
operator|.
name|joinGroups
argument_list|(
name|groups
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|patchSet
operator|.
name|pushCertificate
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setPushCertificate
argument_list|)
expr_stmt|;
name|patchSet
operator|.
name|description
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setDescription
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|fromProto (Entities.PatchSet proto)
specifier|public
name|PatchSet
name|fromProto
parameter_list|(
name|Entities
operator|.
name|PatchSet
name|proto
parameter_list|)
block|{
name|PatchSet
operator|.
name|Builder
name|builder
init|=
name|PatchSet
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
name|patchSetIdConverter
operator|.
name|fromProto
argument_list|(
name|proto
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|groups
argument_list|(
name|proto
operator|.
name|hasGroups
argument_list|()
condition|?
name|PatchSet
operator|.
name|splitGroups
argument_list|(
name|proto
operator|.
name|getGroups
argument_list|()
argument_list|)
else|:
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|proto
operator|.
name|hasPushCertificate
argument_list|()
condition|)
block|{
name|builder
operator|.
name|pushCertificate
argument_list|(
name|proto
operator|.
name|getPushCertificate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|proto
operator|.
name|hasDescription
argument_list|()
condition|)
block|{
name|builder
operator|.
name|description
argument_list|(
name|proto
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// The following fields used to theoretically be nullable in PatchSet, but in practice no
comment|// production codepath should have ever serialized an instance that was missing one of these
comment|// fields.
comment|//
comment|// However, since some protos may theoretically be missing these fields, we need to support
comment|// them. Populate specific sentinel values for each field as documented in the PatchSet javadoc.
comment|// Callers that encounter one of these sentinels will likely fail, for example by failing to
comment|// look up the zeroId. They would have also failed back when the fields were nullable, for
comment|// example with NPE; the current behavior just fails slightly differently.
name|builder
operator|.
name|commitId
argument_list|(
name|proto
operator|.
name|hasCommitId
argument_list|()
condition|?
name|objectIdConverter
operator|.
name|fromProto
argument_list|(
name|proto
operator|.
name|getCommitId
argument_list|()
argument_list|)
else|:
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
operator|.
name|uploader
argument_list|(
name|proto
operator|.
name|hasUploaderAccountId
argument_list|()
condition|?
name|accountIdConverter
operator|.
name|fromProto
argument_list|(
name|proto
operator|.
name|getUploaderAccountId
argument_list|()
argument_list|)
else|:
name|Account
operator|.
name|id
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|createdOn
argument_list|(
name|proto
operator|.
name|hasCreatedOn
argument_list|()
condition|?
operator|new
name|Timestamp
argument_list|(
name|proto
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
else|:
operator|new
name|Timestamp
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getParser ()
specifier|public
name|Parser
argument_list|<
name|Entities
operator|.
name|PatchSet
argument_list|>
name|getParser
parameter_list|()
block|{
return|return
name|Entities
operator|.
name|PatchSet
operator|.
name|parser
argument_list|()
return|;
block|}
block|}
end_enum

end_unit

