begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.prettify.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
operator|.
name|common
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtml
import|;
end_import

begin_interface
DECL|interface|SparseHtmlFile
specifier|public
interface|interface
name|SparseHtmlFile
block|{
comment|/** @return the line of formatted HTML. */
DECL|method|getSafeHtmlLine (int lineNo)
specifier|public
name|SafeHtml
name|getSafeHtmlLine
parameter_list|(
name|int
name|lineNo
parameter_list|)
function_decl|;
comment|/** @return the number of lines in this sparse list. */
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
function_decl|;
comment|/** @return true if the line is valid in this sparse list. */
DECL|method|contains (final int idx)
specifier|public
name|boolean
name|contains
parameter_list|(
specifier|final
name|int
name|idx
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

