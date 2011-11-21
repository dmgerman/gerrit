begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|GitWebConfigTest
specifier|public
class|class
name|GitWebConfigTest
extends|extends
name|TestCase
block|{
DECL|field|VALID_CHARACTERS
specifier|private
specifier|static
specifier|final
name|String
name|VALID_CHARACTERS
init|=
literal|"*()"
decl_stmt|;
DECL|field|SOME_INVALID_CHARACTERS
specifier|private
specifier|static
specifier|final
name|String
name|SOME_INVALID_CHARACTERS
init|=
literal|"09AZaz$-_.+!',"
decl_stmt|;
DECL|method|testValidPathSeparator ()
specifier|public
name|void
name|testValidPathSeparator
parameter_list|()
block|{
for|for
control|(
name|char
name|c
range|:
name|VALID_CHARACTERS
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
literal|"valid character rejected: "
operator|+
name|c
argument_list|,
name|GitWebConfig
operator|.
name|isValidPathSeparator
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testInalidPathSeparator ()
specifier|public
name|void
name|testInalidPathSeparator
parameter_list|()
block|{
for|for
control|(
name|char
name|c
range|:
name|SOME_INVALID_CHARACTERS
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|assertFalse
argument_list|(
literal|"invalid character accepted: "
operator|+
name|c
argument_list|,
name|GitWebConfig
operator|.
name|isValidPathSeparator
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

