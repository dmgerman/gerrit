begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
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
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
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
name|collect
operator|.
name|ImmutableMap
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|NoHttpd
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
name|extensions
operator|.
name|common
operator|.
name|LabelTypeInfo
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
name|extensions
operator|.
name|common
operator|.
name|ProjectInfo
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|GetProjectIT
specifier|public
class|class
name|GetProjectIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|getProject ()
specifier|public
name|void
name|getProject
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|project
operator|.
name|get
argument_list|()
decl_stmt|;
name|ProjectInfo
name|p
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|labels
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|LabelTypeInfo
name|l
init|=
name|p
operator|.
name|labels
operator|.
name|get
argument_list|(
literal|"Code-Review"
argument_list|)
decl_stmt|;
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|want
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|" 0"
argument_list|,
literal|"No score"
argument_list|,
literal|"-1"
argument_list|,
literal|"I would prefer this is not merged as is"
argument_list|,
literal|"-2"
argument_list|,
literal|"This shall not be merged"
argument_list|,
literal|"+1"
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|,
literal|"+2"
argument_list|,
literal|"Looks good to me, approved"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|l
operator|.
name|values
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|want
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|l
operator|.
name|defaultValue
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getProjectWithGitSuffix ()
specifier|public
name|void
name|getProjectWithGitSuffix
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|project
operator|.
name|get
argument_list|()
decl_stmt|;
name|ProjectInfo
name|p
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|p
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getProjectNotExisting ()
specifier|public
name|void
name|getProjectNotExisting
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThrows
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
literal|"does-not-exist"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

