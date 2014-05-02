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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_class
DECL|class|Comment
specifier|public
specifier|abstract
class|class
name|Comment
block|{
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|path
specifier|public
name|String
name|path
decl_stmt|;
DECL|field|side
specifier|public
name|Side
name|side
decl_stmt|;
DECL|field|line
specifier|public
name|int
name|line
decl_stmt|;
DECL|field|range
specifier|public
name|Range
name|range
decl_stmt|;
DECL|field|inReplyTo
specifier|public
name|String
name|inReplyTo
decl_stmt|;
DECL|field|updated
specifier|public
name|Timestamp
name|updated
decl_stmt|;
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
DECL|enum|Side
specifier|public
specifier|static
enum|enum
name|Side
block|{
DECL|enumConstant|PARENT
DECL|enumConstant|REVISION
name|PARENT
block|,
name|REVISION
block|}
DECL|class|Range
specifier|public
specifier|static
class|class
name|Range
block|{
DECL|field|startLine
specifier|public
name|int
name|startLine
decl_stmt|;
DECL|field|startCharacter
specifier|public
name|int
name|startCharacter
decl_stmt|;
DECL|field|endLine
specifier|public
name|int
name|endLine
decl_stmt|;
DECL|field|endCharacter
specifier|public
name|int
name|endCharacter
decl_stmt|;
block|}
block|}
end_class

end_unit

