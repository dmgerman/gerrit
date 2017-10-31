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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
DECL|class|QueryList
specifier|public
class|class
name|QueryList
extends|extends
name|TabFile
block|{
DECL|field|FILE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|FILE_NAME
init|=
literal|"queries"
decl_stmt|;
DECL|field|queriesByName
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queriesByName
decl_stmt|;
DECL|method|QueryList (List<Row> queriesByName)
specifier|private
name|QueryList
parameter_list|(
name|List
argument_list|<
name|Row
argument_list|>
name|queriesByName
parameter_list|)
block|{
name|this
operator|.
name|queriesByName
operator|=
name|toMap
argument_list|(
name|queriesByName
argument_list|)
expr_stmt|;
block|}
DECL|method|parse (String text, ValidationError.Sink errors)
specifier|public
specifier|static
name|QueryList
name|parse
parameter_list|(
name|String
name|text
parameter_list|,
name|ValidationError
operator|.
name|Sink
name|errors
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|QueryList
argument_list|(
name|parse
argument_list|(
name|text
argument_list|,
name|FILE_NAME
argument_list|,
name|TRIM
argument_list|,
name|TRIM
argument_list|,
name|errors
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getQuery (String name)
specifier|public
name|String
name|getQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|queriesByName
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|asText ()
specifier|public
name|String
name|asText
parameter_list|()
block|{
return|return
name|asText
argument_list|(
literal|"Name"
argument_list|,
literal|"Query"
argument_list|,
name|queriesByName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

