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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|EditPreferencesInfo
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
name|GeneralPreferencesInfo
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
name|json
operator|.
name|OutputFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
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
DECL|class|PreferencesTest
specifier|public
class|class
name|PreferencesTest
block|{
DECL|field|GSON
specifier|private
specifier|static
specifier|final
name|Gson
name|GSON
init|=
name|OutputFormat
operator|.
name|JSON_COMPACT
operator|.
name|newGson
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|generalPreferencesRoundTrip ()
specifier|public
name|void
name|generalPreferencesRoundTrip
parameter_list|()
block|{
name|GeneralPreferencesInfo
name|original
init|=
name|GeneralPreferencesInfo
operator|.
name|defaults
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|GSON
operator|.
name|toJson
argument_list|(
name|original
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|GSON
operator|.
name|toJson
argument_list|(
name|Preferences
operator|.
name|General
operator|.
name|fromInfo
argument_list|(
name|original
argument_list|)
operator|.
name|toInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|diffPreferencesRoundTrip ()
specifier|public
name|void
name|diffPreferencesRoundTrip
parameter_list|()
block|{
name|DiffPreferencesInfo
name|original
init|=
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|GSON
operator|.
name|toJson
argument_list|(
name|original
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|GSON
operator|.
name|toJson
argument_list|(
name|Preferences
operator|.
name|Diff
operator|.
name|fromInfo
argument_list|(
name|original
argument_list|)
operator|.
name|toInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|editPreferencesRoundTrip ()
specifier|public
name|void
name|editPreferencesRoundTrip
parameter_list|()
block|{
name|EditPreferencesInfo
name|original
init|=
name|EditPreferencesInfo
operator|.
name|defaults
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|GSON
operator|.
name|toJson
argument_list|(
name|original
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|GSON
operator|.
name|toJson
argument_list|(
name|Preferences
operator|.
name|Edit
operator|.
name|fromInfo
argument_list|(
name|original
argument_list|)
operator|.
name|toInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

