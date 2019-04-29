begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
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
name|Supplier
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
name|Change
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
name|Project
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
name|change
operator|.
name|ChangeKeyAdapter
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
name|Provider
import|;
end_import

begin_class
DECL|class|EventGsonProvider
specifier|public
class|class
name|EventGsonProvider
implements|implements
name|Provider
argument_list|<
name|Gson
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Gson
name|get
parameter_list|()
block|{
return|return
operator|new
name|GsonBuilder
argument_list|()
operator|.
name|registerTypeAdapter
argument_list|(
name|Event
operator|.
name|class
argument_list|,
operator|new
name|EventDeserializer
argument_list|()
argument_list|)
operator|.
name|registerTypeAdapter
argument_list|(
name|Supplier
operator|.
name|class
argument_list|,
operator|new
name|SupplierSerializer
argument_list|()
argument_list|)
operator|.
name|registerTypeAdapter
argument_list|(
name|Supplier
operator|.
name|class
argument_list|,
operator|new
name|SupplierDeserializer
argument_list|()
argument_list|)
operator|.
name|registerTypeAdapter
argument_list|(
name|Change
operator|.
name|Key
operator|.
name|class
argument_list|,
operator|new
name|ChangeKeyAdapter
argument_list|()
argument_list|)
operator|.
name|registerTypeAdapter
argument_list|(
name|Project
operator|.
name|NameKey
operator|.
name|class
argument_list|,
operator|new
name|ProjectNameKeyAdapter
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit

