begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
package|;
end_package

begin_comment
comment|/** A line of a file in a side-by-side view. */
end_comment

begin_class
DECL|class|SideBySideLine
specifier|public
class|class
name|SideBySideLine
extends|extends
name|LineWithComments
block|{
DECL|enum|Type
specifier|public
specifier|static
enum|enum
name|Type
block|{
DECL|enumConstant|DELETE
DECL|enumConstant|INSERT
DECL|enumConstant|EQUAL
name|DELETE
block|,
name|INSERT
block|,
name|EQUAL
block|;   }
DECL|field|lineNumber
specifier|protected
name|int
name|lineNumber
decl_stmt|;
DECL|field|type
specifier|protected
name|SideBySideLine
operator|.
name|Type
name|type
decl_stmt|;
DECL|field|text
specifier|protected
name|String
name|text
decl_stmt|;
DECL|method|SideBySideLine ()
specifier|protected
name|SideBySideLine
parameter_list|()
block|{   }
DECL|method|SideBySideLine (final int line, final SideBySideLine.Type t, final String s)
specifier|public
name|SideBySideLine
parameter_list|(
specifier|final
name|int
name|line
parameter_list|,
specifier|final
name|SideBySideLine
operator|.
name|Type
name|t
parameter_list|,
specifier|final
name|String
name|s
parameter_list|)
block|{
name|lineNumber
operator|=
name|line
expr_stmt|;
name|type
operator|=
name|t
expr_stmt|;
name|text
operator|=
name|s
expr_stmt|;
block|}
DECL|method|getLineNumber ()
specifier|public
name|int
name|getLineNumber
parameter_list|()
block|{
return|return
name|lineNumber
return|;
block|}
DECL|method|getType ()
specifier|public
name|SideBySideLine
operator|.
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
block|}
end_class

end_unit

