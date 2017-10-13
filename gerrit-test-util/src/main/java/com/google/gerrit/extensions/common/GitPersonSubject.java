begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
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
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_class
DECL|class|GitPersonSubject
specifier|public
class|class
name|GitPersonSubject
extends|extends
name|Subject
argument_list|<
name|GitPersonSubject
argument_list|,
name|GitPerson
argument_list|>
block|{
DECL|method|assertThat (GitPerson gitPerson)
specifier|public
specifier|static
name|GitPersonSubject
name|assertThat
parameter_list|(
name|GitPerson
name|gitPerson
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|GitPersonSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|gitPerson
argument_list|)
return|;
block|}
DECL|method|GitPersonSubject (FailureMetadata failureMetadata, GitPerson gitPerson)
specifier|private
name|GitPersonSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|GitPerson
name|gitPerson
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|gitPerson
argument_list|)
expr_stmt|;
block|}
DECL|method|creationDate ()
specifier|public
name|ComparableSubject
argument_list|<
name|?
argument_list|,
name|Timestamp
argument_list|>
name|creationDate
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|GitPerson
name|gitPerson
init|=
name|actual
argument_list|()
decl_stmt|;
return|return
name|Truth
operator|.
name|assertThat
argument_list|(
name|gitPerson
operator|.
name|date
argument_list|)
operator|.
name|named
argument_list|(
literal|"creationDate"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

