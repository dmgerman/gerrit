begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
package|;
end_package

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
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
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
name|BadRequestException
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
name|JsonArray
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
name|JsonObject
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
name|JsonPrimitive
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

begin_class
DECL|class|ParameterParserTest
specifier|public
class|class
name|ParameterParserTest
block|{
annotation|@
name|Test
DECL|method|testConvertFormToJson ()
specifier|public
name|void
name|testConvertFormToJson
parameter_list|()
throws|throws
name|BadRequestException
block|{
name|JsonObject
name|obj
init|=
name|ParameterParser
operator|.
name|formToJson
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"message"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"this.is.text"
block|}
argument_list|,
literal|"labels.Verified"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"-1"
block|}
argument_list|,
literal|"labels.Code-Review"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"2"
block|}
argument_list|,
literal|"a_list"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|}
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"q"
argument_list|)
argument_list|)
decl_stmt|;
name|JsonObject
name|labels
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|labels
operator|.
name|addProperty
argument_list|(
literal|"Verified"
argument_list|,
literal|"-1"
argument_list|)
expr_stmt|;
name|labels
operator|.
name|addProperty
argument_list|(
literal|"Code-Review"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|JsonArray
name|list
init|=
operator|new
name|JsonArray
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|JsonPrimitive
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|JsonPrimitive
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|JsonObject
name|exp
init|=
operator|new
name|JsonObject
argument_list|()
decl_stmt|;
name|exp
operator|.
name|addProperty
argument_list|(
literal|"message"
argument_list|,
literal|"this.is.text"
argument_list|)
expr_stmt|;
name|exp
operator|.
name|add
argument_list|(
literal|"labels"
argument_list|,
name|labels
argument_list|)
expr_stmt|;
name|exp
operator|.
name|add
argument_list|(
literal|"a_list"
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exp
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

