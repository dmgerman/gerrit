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
DECL|package|com.google.gerrit.jgit.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|jgit
operator|.
name|diff
package|;
end_package

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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
import|;
end_import

begin_class
DECL|class|ReplaceEdit
specifier|public
class|class
name|ReplaceEdit
extends|extends
name|Edit
block|{
DECL|field|internalEdit
specifier|private
name|List
argument_list|<
name|Edit
argument_list|>
name|internalEdit
decl_stmt|;
DECL|method|ReplaceEdit (int as, int ae, int bs, int be, List<Edit> internal)
specifier|public
name|ReplaceEdit
parameter_list|(
name|int
name|as
parameter_list|,
name|int
name|ae
parameter_list|,
name|int
name|bs
parameter_list|,
name|int
name|be
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|internal
parameter_list|)
block|{
name|super
argument_list|(
name|as
argument_list|,
name|ae
argument_list|,
name|bs
argument_list|,
name|be
argument_list|)
expr_stmt|;
name|internalEdit
operator|=
name|internal
expr_stmt|;
block|}
DECL|method|ReplaceEdit (Edit orig, List<Edit> internal)
specifier|public
name|ReplaceEdit
parameter_list|(
name|Edit
name|orig
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|internal
parameter_list|)
block|{
name|super
argument_list|(
name|orig
operator|.
name|getBeginA
argument_list|()
argument_list|,
name|orig
operator|.
name|getEndA
argument_list|()
argument_list|,
name|orig
operator|.
name|getBeginB
argument_list|()
argument_list|,
name|orig
operator|.
name|getEndB
argument_list|()
argument_list|)
expr_stmt|;
name|internalEdit
operator|=
name|internal
expr_stmt|;
block|}
DECL|method|getInternalEdits ()
specifier|public
name|List
argument_list|<
name|Edit
argument_list|>
name|getInternalEdits
parameter_list|()
block|{
return|return
name|internalEdit
return|;
block|}
block|}
end_class

end_unit

