begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|Gson
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
name|GsonBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
annotation|@
name|Singleton
DECL|class|ChangeNoteJson
specifier|public
class|class
name|ChangeNoteJson
block|{
DECL|field|gson
specifier|private
specifier|final
name|Gson
name|gson
init|=
name|newGson
argument_list|()
decl_stmt|;
DECL|method|newGson ()
specifier|static
name|Gson
name|newGson
parameter_list|()
block|{
return|return
operator|new
name|GsonBuilder
argument_list|()
operator|.
name|registerTypeAdapter
argument_list|(
name|Timestamp
operator|.
name|class
argument_list|,
operator|new
name|CommentTimestampAdapter
argument_list|()
operator|.
name|nullSafe
argument_list|()
argument_list|)
operator|.
name|setPrettyPrinting
argument_list|()
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|getGson ()
specifier|public
name|Gson
name|getGson
parameter_list|()
block|{
return|return
name|gson
return|;
block|}
block|}
end_class

end_unit

