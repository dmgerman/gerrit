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
DECL|package|com.google.gerrit.common.data.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertAbout
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|ComparableSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|FailureMetadata
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|StringSubject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Subject
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
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|AccountGroup
import|;
end_import

begin_class
DECL|class|GroupReferenceSubject
specifier|public
class|class
name|GroupReferenceSubject
extends|extends
name|Subject
block|{
DECL|method|assertThat (GroupReference group)
specifier|public
specifier|static
name|GroupReferenceSubject
name|assertThat
parameter_list|(
name|GroupReference
name|group
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|groupReferences
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|group
argument_list|)
return|;
block|}
DECL|method|groupReferences ()
specifier|public
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|GroupReferenceSubject
argument_list|,
name|GroupReference
argument_list|>
name|groupReferences
parameter_list|()
block|{
return|return
name|GroupReferenceSubject
operator|::
operator|new
return|;
block|}
DECL|field|group
specifier|private
specifier|final
name|GroupReference
name|group
decl_stmt|;
DECL|method|GroupReferenceSubject (FailureMetadata metadata, GroupReference group)
specifier|private
name|GroupReferenceSubject
parameter_list|(
name|FailureMetadata
name|metadata
parameter_list|,
name|GroupReference
name|group
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
DECL|method|groupUuid ()
specifier|public
name|ComparableSubject
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupUuid
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getUUID()"
argument_list|)
operator|.
name|that
argument_list|(
name|group
operator|.
name|getUUID
argument_list|()
argument_list|)
return|;
block|}
DECL|method|name ()
specifier|public
name|StringSubject
name|name
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getName()"
argument_list|)
operator|.
name|that
argument_list|(
name|group
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

