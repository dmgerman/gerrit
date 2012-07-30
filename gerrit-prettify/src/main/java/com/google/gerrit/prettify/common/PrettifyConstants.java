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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|i18n
operator|.
name|client
operator|.
name|Constants
import|;
end_import

begin_interface
DECL|interface|PrettifyConstants
specifier|public
interface|interface
name|PrettifyConstants
extends|extends
name|Constants
block|{
DECL|field|C
specifier|static
specifier|final
name|PrettifyConstants
name|C
init|=
name|GWT
operator|.
name|create
argument_list|(
name|PrettifyConstants
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|wseTabAfterSpace ()
name|String
name|wseTabAfterSpace
parameter_list|()
function_decl|;
DECL|method|wseTrailingSpace ()
name|String
name|wseTrailingSpace
parameter_list|()
function_decl|;
DECL|method|wseBareCR ()
name|String
name|wseBareCR
parameter_list|()
function_decl|;
DECL|method|leCR ()
name|String
name|leCR
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

