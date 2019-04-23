begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.api.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
operator|.
name|config
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
name|assertWithMessage
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
name|acceptance
operator|.
name|AssertUtil
operator|.
name|assertPrefs
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
name|client
operator|.
name|DiffPreferencesInfo
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
DECL|class|DiffPreferencesIT
specifier|public
class|class
name|DiffPreferencesIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|getDiffPreferences ()
specifier|public
name|void
name|getDiffPreferences
parameter_list|()
throws|throws
name|Exception
block|{
name|DiffPreferencesInfo
name|result
init|=
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|getDefaultDiffPreferences
argument_list|()
decl_stmt|;
name|assertPrefs
argument_list|(
name|result
argument_list|,
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setDiffPreferences ()
specifier|public
name|void
name|setDiffPreferences
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|newLineLength
init|=
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
operator|.
name|lineLength
operator|+
literal|10
decl_stmt|;
name|DiffPreferencesInfo
name|update
init|=
operator|new
name|DiffPreferencesInfo
argument_list|()
decl_stmt|;
name|update
operator|.
name|lineLength
operator|=
name|newLineLength
expr_stmt|;
name|DiffPreferencesInfo
name|result
init|=
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|setDefaultDiffPreferences
argument_list|(
name|update
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
literal|"lineLength"
argument_list|)
operator|.
name|that
argument_list|(
name|result
operator|.
name|lineLength
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|newLineLength
argument_list|)
expr_stmt|;
name|result
operator|=
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|getDefaultDiffPreferences
argument_list|()
expr_stmt|;
name|DiffPreferencesInfo
name|expected
init|=
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
decl_stmt|;
name|expected
operator|.
name|lineLength
operator|=
name|newLineLength
expr_stmt|;
name|assertPrefs
argument_list|(
name|result
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

