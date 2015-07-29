begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|gpg
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|OBJ_BLOB
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
name|RefNames
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|ArmoredInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|ArmoredOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyRing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyRingCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|bc
operator|.
name|BcPGPObjectFactory
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
name|lib
operator|.
name|CommitBuilder
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
name|lib
operator|.
name|Constants
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
name|lib
operator|.
name|ObjectId
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
name|lib
operator|.
name|ObjectInserter
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
name|lib
operator|.
name|ObjectReader
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
name|lib
operator|.
name|Ref
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
name|lib
operator|.
name|RefUpdate
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
name|lib
operator|.
name|Repository
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
name|notes
operator|.
name|Note
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
name|notes
operator|.
name|NoteMap
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * Store of GPG public keys in git notes.  *<p>  * Keys are stored in filenames based on their hex key ID, padded out to 40  * characters to match the length of a SHA-1. (This is to easily reuse existing  * fanout code in {@link NoteMap}, and may be changed later after an appropriate  * transition.)  *<p>  * The contents of each file is an ASCII armored stream containing one or more  * public key rings matching the ID. Multiple keys are supported because forging  * a key ID is possible, but such a key cannot be used to verify signatures  * produced with the correct key.  *<p>  * No additional checks are performed on the key after reading; callers should  * only trust keys after checking with a {@link PublicKeyChecker}.  */
end_comment

begin_class
DECL|class|PublicKeyStore
specifier|public
class|class
name|PublicKeyStore
implements|implements
name|AutoCloseable
block|{
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|reader
specifier|private
name|ObjectReader
name|reader
decl_stmt|;
DECL|field|tip
specifier|private
name|RevCommit
name|tip
decl_stmt|;
DECL|field|notes
specifier|private
name|NoteMap
name|notes
decl_stmt|;
DECL|field|pending
specifier|private
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|pending
decl_stmt|;
comment|/** @param repo repository to read keys from. */
DECL|method|PublicKeyStore (Repository repo)
specifier|public
name|PublicKeyStore
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|pending
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|reader
operator|=
literal|null
expr_stmt|;
name|notes
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|load ()
specifier|private
name|void
name|load
parameter_list|()
throws|throws
name|IOException
block|{
name|close
argument_list|()
expr_stmt|;
name|reader
operator|=
name|repo
operator|.
name|newObjectReader
argument_list|()
expr_stmt|;
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|REFS_GPG_KEYS
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
name|tip
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|notes
operator|=
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|tip
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Read public keys with the given key ID.    *<p>    * Keys should not be trusted unless checked with {@link PublicKeyChecker}.    *<p>    * Multiple calls to this method use the same state of the key ref; to reread    * the ref, call {@link #close()} first.    *    * @param keyId key ID.    * @return any keys found that could be successfully parsed.    * @throws PGPException if an error occurred parsing the key data.    * @throws IOException if an error occurred reading the repository data.    */
DECL|method|get (long keyId)
specifier|public
name|PGPPublicKeyRingCollection
name|get
parameter_list|(
name|long
name|keyId
parameter_list|)
throws|throws
name|PGPException
throws|,
name|IOException
block|{
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
name|load
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|notes
operator|==
literal|null
condition|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
name|Note
name|note
init|=
name|notes
operator|.
name|getNote
argument_list|(
name|keyObjectId
argument_list|(
name|keyId
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|note
operator|==
literal|null
condition|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|reader
operator|.
name|open
argument_list|(
name|note
operator|.
name|getData
argument_list|()
argument_list|,
name|OBJ_BLOB
argument_list|)
operator|.
name|openStream
argument_list|()
init|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
operator|new
name|BcPGPObjectFactory
argument_list|(
operator|new
name|ArmoredInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
break|break;
block|}
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|PGPPublicKeyRing
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
operator|(
name|PGPPublicKeyRing
operator|)
name|obj
argument_list|)
expr_stmt|;
block|}
name|checkState
argument_list|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"expected one PGP object per ArmoredInputStream"
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PGPPublicKeyRingCollection
argument_list|(
name|keys
argument_list|)
return|;
block|}
block|}
comment|/**    * Add a public key to the store.    *<p>    * Multiple calls may be made to buffer keys in memory, and they are not saved    * until {@link #save(CommitBuilder)} is called.    *    * @param keyRing a key ring containing exactly one public master key.    */
DECL|method|add (PGPPublicKeyRing keyRing)
specifier|public
name|void
name|add
parameter_list|(
name|PGPPublicKeyRing
name|keyRing
parameter_list|)
block|{
name|int
name|numMaster
init|=
literal|0
decl_stmt|;
for|for
control|(
name|PGPPublicKey
name|key
range|:
name|keyRing
control|)
block|{
if|if
condition|(
name|key
operator|.
name|isMasterKey
argument_list|()
condition|)
block|{
name|numMaster
operator|++
expr_stmt|;
block|}
block|}
comment|// We could have an additional sanity check to ensure all subkeys belong to
comment|// this master key, but that requires doing actual signature verification
comment|// here. The alternative is insane but harmless.
if|if
condition|(
name|numMaster
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Exactly 1 master key is required, found "
operator|+
name|numMaster
argument_list|)
throw|;
block|}
name|pending
operator|.
name|add
argument_list|(
name|keyRing
argument_list|)
expr_stmt|;
block|}
comment|/**    * Save pending keys to the store.    *<p>    * One commit is created and the ref updated. The pending list is cleared if    * and only if the ref update succeeds, which allows for easy retries in case    * of lock failure.    *    * @param cb commit builder with at least author and identity populated; tree    *     and parent are ignored.    * @return result of the ref update.    */
DECL|method|save (CommitBuilder cb)
specifier|public
name|RefUpdate
operator|.
name|Result
name|save
parameter_list|(
name|CommitBuilder
name|cb
parameter_list|)
throws|throws
name|PGPException
throws|,
name|IOException
block|{
if|if
condition|(
name|pending
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|RefUpdate
operator|.
name|Result
operator|.
name|NO_CHANGE
return|;
block|}
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
name|load
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|notes
operator|==
literal|null
condition|)
block|{
name|notes
operator|=
name|NoteMap
operator|.
name|newEmptyMap
argument_list|()
expr_stmt|;
block|}
name|ObjectId
name|newTip
decl_stmt|;
try|try
init|(
name|ObjectInserter
name|ins
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
for|for
control|(
name|PGPPublicKeyRing
name|keyRing
range|:
name|pending
control|)
block|{
name|saveToNotes
argument_list|(
name|ins
argument_list|,
name|keyRing
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tip
operator|!=
literal|null
condition|)
block|{
name|cb
operator|.
name|setParentId
argument_list|(
name|tip
argument_list|)
expr_stmt|;
block|}
name|cb
operator|.
name|setTreeId
argument_list|(
name|notes
operator|.
name|writeTree
argument_list|(
name|ins
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|cb
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
name|cb
operator|.
name|setMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Update %d public key%s"
argument_list|,
name|pending
operator|.
name|size
argument_list|()
argument_list|,
name|pending
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|?
literal|"s"
else|:
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|newTip
operator|=
name|ins
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
expr_stmt|;
name|ins
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|RefNames
operator|.
name|REFS_GPG_KEYS
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|tip
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|newTip
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogIdent
argument_list|(
name|cb
operator|.
name|getCommitter
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setRefLogMessage
argument_list|(
literal|"Store public keys"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|RefUpdate
operator|.
name|Result
name|result
init|=
name|ru
operator|.
name|update
argument_list|()
decl_stmt|;
name|close
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|FAST_FORWARD
case|:
case|case
name|NEW
case|:
case|case
name|NO_CHANGE
case|:
name|pending
operator|.
name|clear
argument_list|()
expr_stmt|;
break|break;
default|default:
break|break;
block|}
return|return
name|result
return|;
block|}
DECL|method|saveToNotes (ObjectInserter ins, PGPPublicKeyRing keyRing)
specifier|private
name|void
name|saveToNotes
parameter_list|(
name|ObjectInserter
name|ins
parameter_list|,
name|PGPPublicKeyRing
name|keyRing
parameter_list|)
throws|throws
name|PGPException
throws|,
name|IOException
block|{
name|long
name|keyId
init|=
name|keyRing
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getKeyID
argument_list|()
decl_stmt|;
name|PGPPublicKeyRingCollection
name|existing
init|=
name|get
argument_list|(
name|keyId
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|toWrite
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|existing
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|boolean
name|replaced
init|=
literal|false
decl_stmt|;
for|for
control|(
name|PGPPublicKeyRing
name|kr
range|:
name|existing
control|)
block|{
if|if
condition|(
name|sameKey
argument_list|(
name|keyRing
argument_list|,
name|kr
argument_list|)
condition|)
block|{
name|toWrite
operator|.
name|add
argument_list|(
name|keyRing
argument_list|)
expr_stmt|;
name|replaced
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|toWrite
operator|.
name|add
argument_list|(
name|kr
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|replaced
condition|)
block|{
name|toWrite
operator|.
name|add
argument_list|(
name|keyRing
argument_list|)
expr_stmt|;
block|}
name|notes
operator|.
name|set
argument_list|(
name|keyObjectId
argument_list|(
name|keyId
argument_list|)
argument_list|,
name|ins
operator|.
name|insert
argument_list|(
name|OBJ_BLOB
argument_list|,
name|keysToArmored
argument_list|(
name|toWrite
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|sameKey (PGPPublicKeyRing kr1, PGPPublicKeyRing kr2)
specifier|private
specifier|static
name|boolean
name|sameKey
parameter_list|(
name|PGPPublicKeyRing
name|kr1
parameter_list|,
name|PGPPublicKeyRing
name|kr2
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|kr1
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getFingerprint
argument_list|()
argument_list|,
name|kr2
operator|.
name|getPublicKey
argument_list|()
operator|.
name|getFingerprint
argument_list|()
argument_list|)
return|;
block|}
DECL|method|keysToArmored (List<PGPPublicKeyRing> keys)
specifier|private
specifier|static
name|byte
index|[]
name|keysToArmored
parameter_list|(
name|List
argument_list|<
name|PGPPublicKeyRing
argument_list|>
name|keys
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|4096
operator|*
name|keys
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PGPPublicKeyRing
name|kr
range|:
name|keys
control|)
block|{
try|try
init|(
name|ArmoredOutputStream
name|aout
init|=
operator|new
name|ArmoredOutputStream
argument_list|(
name|out
argument_list|)
init|)
block|{
name|kr
operator|.
name|encode
argument_list|(
name|aout
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|empty ()
specifier|private
specifier|static
name|PGPPublicKeyRingCollection
name|empty
parameter_list|()
throws|throws
name|PGPException
throws|,
name|IOException
block|{
return|return
operator|new
name|PGPPublicKeyRingCollection
argument_list|(
name|Collections
operator|.
expr|<
name|PGPPublicKeyRing
operator|>
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|keyToString (PGPPublicKey key)
specifier|static
name|String
name|keyToString
parameter_list|(
name|PGPPublicKey
name|key
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|key
operator|.
name|getUserIDs
argument_list|()
decl_stmt|;
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|key
operator|.
name|getFingerprint
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s %s(%04X %04X %04X %04X %04X  %04X %04X %04X %04X %04X)"
argument_list|,
name|keyIdToString
argument_list|(
name|key
operator|.
name|getKeyID
argument_list|()
argument_list|)
argument_list|,
name|it
operator|.
name|hasNext
argument_list|()
condition|?
name|it
operator|.
name|next
argument_list|()
operator|+
literal|" "
else|:
literal|""
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|)
return|;
block|}
DECL|method|keyIdToString (long keyId)
specifier|static
name|String
name|keyIdToString
parameter_list|(
name|long
name|keyId
parameter_list|)
block|{
comment|// Match key ID format from gpg --list-keys.
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%08X"
argument_list|,
operator|(
name|int
operator|)
name|keyId
argument_list|)
return|;
block|}
DECL|method|keyObjectId (long keyId)
specifier|static
name|ObjectId
name|keyObjectId
parameter_list|(
name|long
name|keyId
parameter_list|)
block|{
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
operator|new
name|byte
index|[
name|Constants
operator|.
name|OBJECT_ID_LENGTH
index|]
argument_list|)
decl_stmt|;
name|buf
operator|.
name|putLong
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
return|return
name|ObjectId
operator|.
name|fromRaw
argument_list|(
name|buf
operator|.
name|array
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

