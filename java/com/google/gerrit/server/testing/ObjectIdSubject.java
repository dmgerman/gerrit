begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|Subject
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
name|Truth
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

begin_class
DECL|class|ObjectIdSubject
specifier|public
class|class
name|ObjectIdSubject
extends|extends
name|Subject
argument_list|<
name|ObjectIdSubject
argument_list|,
name|ObjectId
argument_list|>
block|{
DECL|method|assertThat (ObjectId objectId)
specifier|public
specifier|static
name|ObjectIdSubject
name|assertThat
parameter_list|(
name|ObjectId
name|objectId
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|ObjectIdSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|objectId
argument_list|)
return|;
block|}
DECL|method|ObjectIdSubject (FailureMetadata metadata, ObjectId actual)
specifier|private
name|ObjectIdSubject
parameter_list|(
name|FailureMetadata
name|metadata
parameter_list|,
name|ObjectId
name|actual
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|hasName (String expectedName)
specifier|public
name|void
name|hasName
parameter_list|(
name|String
name|expectedName
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ObjectId
name|objectId
init|=
name|actual
argument_list|()
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|objectId
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"name"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

