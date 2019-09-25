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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verifyNoMoreInteractions
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
name|reviewdb
operator|.
name|client
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
name|server
operator|.
name|git
operator|.
name|ValidationError
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
name|Config
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

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_comment
comment|/** Tests for parsing user preferences from Git. */
end_comment

begin_class
DECL|class|StoredPreferencesTest
specifier|public
class|class
name|StoredPreferencesTest
block|{
DECL|enum|Unknown
enum|enum
name|Unknown
block|{
DECL|enumConstant|STATE
name|STATE
block|}
annotation|@
name|Test
DECL|method|ignoreUnknownAccountPreferencesWhenParsing ()
specifier|public
name|void
name|ignoreUnknownAccountPreferencesWhenParsing
parameter_list|()
block|{
name|ValidationError
operator|.
name|Sink
name|errorSink
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|ValidationError
operator|.
name|Sink
operator|.
name|class
argument_list|)
decl_stmt|;
name|StoredPreferences
name|preferences
init|=
operator|new
name|StoredPreferences
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|1
argument_list|)
argument_list|,
name|configWithUnknownEntries
argument_list|()
argument_list|,
operator|new
name|Config
argument_list|()
argument_list|,
name|errorSink
argument_list|)
decl_stmt|;
name|GeneralPreferencesInfo
name|parsedPreferences
init|=
name|preferences
operator|.
name|getGeneralPreferences
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|parsedPreferences
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|parsedPreferences
operator|.
name|expandInlineDiffs
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|verifyNoMoreInteractions
argument_list|(
name|errorSink
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ignoreUnknownDefaultAccountPreferencesWhenParsing ()
specifier|public
name|void
name|ignoreUnknownDefaultAccountPreferencesWhenParsing
parameter_list|()
block|{
name|ValidationError
operator|.
name|Sink
name|errorSink
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|ValidationError
operator|.
name|Sink
operator|.
name|class
argument_list|)
decl_stmt|;
name|StoredPreferences
name|preferences
init|=
operator|new
name|StoredPreferences
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|Config
argument_list|()
argument_list|,
name|configWithUnknownEntries
argument_list|()
argument_list|,
name|errorSink
argument_list|)
decl_stmt|;
name|GeneralPreferencesInfo
name|parsedPreferences
init|=
name|preferences
operator|.
name|getGeneralPreferences
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|parsedPreferences
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|parsedPreferences
operator|.
name|expandInlineDiffs
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|verifyNoMoreInteractions
argument_list|(
name|errorSink
argument_list|)
expr_stmt|;
block|}
DECL|method|configWithUnknownEntries ()
specifier|private
specifier|static
name|Config
name|configWithUnknownEntries
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"general"
argument_list|,
literal|null
argument_list|,
literal|"expandInlineDiffs"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"general"
argument_list|,
literal|null
argument_list|,
literal|"unknown"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setEnum
argument_list|(
literal|"general"
argument_list|,
literal|null
argument_list|,
literal|"unknownenum"
argument_list|,
name|Unknown
operator|.
name|STATE
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
literal|"general"
argument_list|,
literal|null
argument_list|,
literal|"unknownstring"
argument_list|,
literal|"bla"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
block|}
end_class

end_unit

