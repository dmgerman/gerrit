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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|base
operator|.
name|Strings
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
name|AuthException
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
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|RestReadView
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
name|PatchLineComment
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
name|util
operator|.
name|Url
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

begin_class
DECL|class|GetDraft
class|class
name|GetDraft
implements|implements
name|RestReadView
argument_list|<
name|DraftResource
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (DraftResource rsrc)
specifier|public
name|Object
name|apply
parameter_list|(
name|DraftResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|Exception
block|{
return|return
operator|new
name|Comment
argument_list|(
name|rsrc
operator|.
name|getComment
argument_list|()
argument_list|)
return|;
block|}
DECL|enum|Side
specifier|static
enum|enum
name|Side
block|{
DECL|enumConstant|PARENT
DECL|enumConstant|REVISION
name|PARENT
block|,
name|REVISION
block|;   }
DECL|class|Comment
specifier|static
class|class
name|Comment
block|{
DECL|field|kind
specifier|final
name|String
name|kind
init|=
literal|"gerritcodereview#comment"
decl_stmt|;
DECL|field|id
name|String
name|id
decl_stmt|;
DECL|field|path
name|String
name|path
decl_stmt|;
DECL|field|side
name|Side
name|side
decl_stmt|;
DECL|field|line
name|Integer
name|line
decl_stmt|;
DECL|field|message
name|String
name|message
decl_stmt|;
DECL|field|updated
name|Timestamp
name|updated
decl_stmt|;
DECL|method|Comment (PatchLineComment c)
name|Comment
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
block|{
name|id
operator|=
name|Url
operator|.
name|encode
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|path
operator|=
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|getFileName
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|getSide
argument_list|()
operator|==
literal|0
condition|)
block|{
name|side
operator|=
name|Side
operator|.
name|PARENT
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|getLine
argument_list|()
operator|>
literal|0
condition|)
block|{
name|line
operator|=
name|c
operator|.
name|getLine
argument_list|()
expr_stmt|;
block|}
name|message
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|c
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|updated
operator|=
name|c
operator|.
name|getWrittenOn
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

