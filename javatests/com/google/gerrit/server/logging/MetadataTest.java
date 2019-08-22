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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
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
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MetadataTest
specifier|public
class|class
name|MetadataTest
block|{
annotation|@
name|Test
DECL|method|stringForLoggingOmitsEmptyOptionalValuesAndReformatsOptionalValuesThatArePresent ()
specifier|public
name|void
name|stringForLoggingOmitsEmptyOptionalValuesAndReformatsOptionalValuesThatArePresent
parameter_list|()
block|{
name|Metadata
name|metadata
init|=
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|accountId
argument_list|(
literal|1000001
argument_list|)
operator|.
name|branchName
argument_list|(
literal|"refs/heads/foo"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|metadata
operator|.
name|toStringForLogging
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Metadata{accountId=1000001, branchName=refs/heads/foo, pluginMetadata=[]}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
DECL|method|stringForLoggingOmitsEmptyOptionalValuesAndReformatsOptionalValuesThatArePresentNoFieldsSet ()
name|stringForLoggingOmitsEmptyOptionalValuesAndReformatsOptionalValuesThatArePresentNoFieldsSet
parameter_list|()
block|{
name|Metadata
name|metadata
init|=
name|Metadata
operator|.
name|builder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|metadata
operator|.
name|toStringForLogging
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Metadata{pluginMetadata=[]}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

