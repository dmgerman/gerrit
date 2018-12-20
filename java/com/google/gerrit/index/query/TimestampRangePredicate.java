begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|FieldDef
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
name|JavaSqlTimestampHelper
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|// TODO: Migrate this to IntegerRangePredicate
end_comment

begin_class
DECL|class|TimestampRangePredicate
specifier|public
specifier|abstract
class|class
name|TimestampRangePredicate
parameter_list|<
name|I
parameter_list|>
extends|extends
name|IndexPredicate
argument_list|<
name|I
argument_list|>
block|{
DECL|method|parse (String value)
specifier|protected
specifier|static
name|Timestamp
name|parse
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|QueryParseException
block|{
try|try
block|{
return|return
name|JavaSqlTimestampHelper
operator|.
name|parseTimestamp
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// parseTimestamp's errors are specific and helpful, so preserve them.
throw|throw
operator|new
name|QueryParseException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|TimestampRangePredicate (FieldDef<I, Timestamp> def, String name, String value)
specifier|protected
name|TimestampRangePredicate
parameter_list|(
name|FieldDef
argument_list|<
name|I
argument_list|,
name|Timestamp
argument_list|>
name|def
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|getMinTimestamp ()
specifier|public
specifier|abstract
name|Date
name|getMinTimestamp
parameter_list|()
function_decl|;
DECL|method|getMaxTimestamp ()
specifier|public
specifier|abstract
name|Date
name|getMaxTimestamp
parameter_list|()
function_decl|;
block|}
end_class

end_unit

