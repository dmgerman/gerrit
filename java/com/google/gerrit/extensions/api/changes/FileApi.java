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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|changes
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
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
operator|.
name|Whitespace
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
name|common
operator|.
name|BlameInfo
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
name|common
operator|.
name|DiffInfo
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
name|BinaryResult
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
name|NotImplementedException
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
name|RestApiException
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
name|OptionalInt
import|;
end_import

begin_interface
DECL|interface|FileApi
specifier|public
interface|interface
name|FileApi
block|{
DECL|method|content ()
name|BinaryResult
name|content
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Diff against the revision's parent version of the file. */
DECL|method|diff ()
name|DiffInfo
name|diff
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** @param base revision id of the revision to be used as the diff base */
DECL|method|diff (String base)
name|DiffInfo
name|diff
parameter_list|(
name|String
name|base
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** @param parent 1-based parent number to diff against */
DECL|method|diff (int parent)
name|DiffInfo
name|diff
parameter_list|(
name|int
name|parent
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Creates a request to retrieve the diff. On the returned request formatting options for the diff    * can be set.    */
DECL|method|diffRequest ()
name|DiffRequest
name|diffRequest
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Set the file reviewed or not reviewed */
DECL|method|setReviewed (boolean reviewed)
name|void
name|setReviewed
parameter_list|(
name|boolean
name|reviewed
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Creates a request to retrieve the blame information. On the returned request formatting options    * for the blame request can be set.    */
DECL|method|blameRequest ()
name|BlameRequest
name|blameRequest
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|class|DiffRequest
specifier|abstract
class|class
name|DiffRequest
block|{
DECL|field|base
specifier|private
name|String
name|base
decl_stmt|;
DECL|field|context
specifier|private
name|Integer
name|context
decl_stmt|;
DECL|field|intraline
specifier|private
name|Boolean
name|intraline
decl_stmt|;
DECL|field|whitespace
specifier|private
name|Whitespace
name|whitespace
decl_stmt|;
DECL|field|parent
specifier|private
name|OptionalInt
name|parent
init|=
name|OptionalInt
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|abstract
name|DiffInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|withBase (String base)
specifier|public
name|DiffRequest
name|withBase
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withContext (int context)
specifier|public
name|DiffRequest
name|withContext
parameter_list|(
name|int
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withIntraline (boolean intraline)
specifier|public
name|DiffRequest
name|withIntraline
parameter_list|(
name|boolean
name|intraline
parameter_list|)
block|{
name|this
operator|.
name|intraline
operator|=
name|intraline
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withWhitespace (Whitespace whitespace)
specifier|public
name|DiffRequest
name|withWhitespace
parameter_list|(
name|Whitespace
name|whitespace
parameter_list|)
block|{
name|this
operator|.
name|whitespace
operator|=
name|whitespace
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withParent (int parent)
specifier|public
name|DiffRequest
name|withParent
parameter_list|(
name|int
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|OptionalInt
operator|.
name|of
argument_list|(
name|parent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getBase ()
specifier|public
name|String
name|getBase
parameter_list|()
block|{
return|return
name|base
return|;
block|}
DECL|method|getContext ()
specifier|public
name|Integer
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getIntraline ()
specifier|public
name|Boolean
name|getIntraline
parameter_list|()
block|{
return|return
name|intraline
return|;
block|}
DECL|method|getWhitespace ()
specifier|public
name|Whitespace
name|getWhitespace
parameter_list|()
block|{
return|return
name|whitespace
return|;
block|}
DECL|method|getParent ()
specifier|public
name|OptionalInt
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
block|}
DECL|class|BlameRequest
specifier|abstract
class|class
name|BlameRequest
block|{
DECL|field|forBase
specifier|private
name|boolean
name|forBase
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|BlameInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|forBase (boolean forBase)
specifier|public
name|BlameRequest
name|forBase
parameter_list|(
name|boolean
name|forBase
parameter_list|)
block|{
name|this
operator|.
name|forBase
operator|=
name|forBase
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|isForBase ()
specifier|public
name|boolean
name|isForBase
parameter_list|()
block|{
return|return
name|forBase
return|;
block|}
block|}
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|FileApi
block|{
annotation|@
name|Override
DECL|method|content ()
specifier|public
name|BinaryResult
name|content
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|diff ()
specifier|public
name|DiffInfo
name|diff
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|diff (String base)
specifier|public
name|DiffInfo
name|diff
parameter_list|(
name|String
name|base
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|diff (int parent)
specifier|public
name|DiffInfo
name|diff
parameter_list|(
name|int
name|parent
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|diffRequest ()
specifier|public
name|DiffRequest
name|diffRequest
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setReviewed (boolean reviewed)
specifier|public
name|void
name|setReviewed
parameter_list|(
name|boolean
name|reviewed
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|blameRequest ()
specifier|public
name|BlameRequest
name|blameRequest
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit

