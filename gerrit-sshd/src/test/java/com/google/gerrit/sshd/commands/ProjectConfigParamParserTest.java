begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|project
operator|.
name|PutConfig
operator|.
name|ConfigValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ProjectConfigParamParserTest
specifier|public
class|class
name|ProjectConfigParamParserTest
block|{
DECL|field|cmd
specifier|private
name|CreateProjectCommand
name|cmd
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|cmd
operator|=
operator|new
name|CreateProjectCommand
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseSingleValue ()
specifier|public
name|void
name|parseSingleValue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|in
init|=
literal|"a.b=c"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
argument_list|>
name|r
init|=
name|cmd
operator|.
name|parsePluginConfigValues
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|ConfigValue
name|configValue
init|=
name|r
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|configValue
operator|.
name|value
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configValue
operator|.
name|values
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|parseMultipleValue ()
specifier|public
name|void
name|parseMultipleValue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|in
init|=
literal|"a.b=c,d,e"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
argument_list|>
name|r
init|=
name|cmd
operator|.
name|parsePluginConfigValues
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|ConfigValue
name|configValue
init|=
name|r
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"c"
block|,
literal|"d"
block|,
literal|"e"
block|}
argument_list|,
name|configValue
operator|.
name|values
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configValue
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

